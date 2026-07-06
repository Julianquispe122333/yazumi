package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.CarritoBusiness;
import com.example.apiyazumy.dto.request.ActualizarCantidadRequestDTO;
import com.example.apiyazumy.dto.request.AgregarAlCarritoRequestDTO;
import com.example.apiyazumy.dto.request.ComprarRequestDTO;
import com.example.apiyazumy.dto.response.CarritoDetalleResponseDTO;
import com.example.apiyazumy.dto.response.CarritoResponseDTO;
import com.example.apiyazumy.dto.response.CompraResponseDTO;
import com.example.apiyazumy.entity.*;
import com.example.apiyazumy.exception.CarritoNoEncontradoException;
import com.example.apiyazumy.exception.CarritoVacioException;
import com.example.apiyazumy.exception.ProductoNoEncontradoException;
import com.example.apiyazumy.exception.StockInsuficienteException;
import com.example.apiyazumy.exception.UsuarioNoEncontradoException;
import com.example.apiyazumy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoBusinessImpl implements CarritoBusiness {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final EstadoPedidoRepository estadoPedidoRepository;

    // ─── AGREGAR PRODUCTO ─────────────────────────────────────────────────────

    @Override
    @Transactional
    public CarritoResponseDTO agregarProducto(AgregarAlCarritoRequestDTO request) {

        // 1. Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("USUARIO_NO_ENCONTRADO"));

        // 2. Validar que el producto existe y está activo
        Producto producto = productoRepository.findById(request.getIdProducto())
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new ProductoNoEncontradoException("PRODUCTO_NO_ENCONTRADO"));

        // 3. Validar stock disponible
        validarStock(producto, request.getCantidad());

        // 4. Obtener o crear el carrito del usuario
        Carrito carrito = obtenerOCrearCarrito(usuario);

        // 5. Verificar si el producto ya existe en el carrito
        carritoDetalleRepository
                .findByCarritoIdCarritoAndProductoIdProducto(carrito.getIdCarrito(), producto.getIdProducto())
                .ifPresentOrElse(
                        detalleExistente -> {
                            // Regla de negocio: si ya existe → incrementar cantidad
                            int nuevaCantidad = detalleExistente.getCantidad() + request.getCantidad();
                            // Revalidar stock con la nueva cantidad total
                            if (nuevaCantidad > producto.getStock()) {
                                throw new StockInsuficienteException(
                                        "STOCK_INSUFICIENTE: stock disponible=" + producto.getStock()
                                        + ", cantidad en carrito=" + detalleExistente.getCantidad()
                                        + ", cantidad solicitada=" + request.getCantidad());
                            }
                            detalleExistente.setCantidad(nuevaCantidad);
                            carritoDetalleRepository.save(detalleExistente);
                        },
                        () -> {
                            // No existe → crear nuevo registro con precio bloqueado al momento de agregar
                            CarritoDetalle nuevoDetalle = CarritoDetalle.builder()
                                    .carrito(carrito)
                                    .producto(producto)
                                    .cantidad(request.getCantidad())
                                    .precioUnitario(producto.getPrecio())  // precio fijo al momento de agregar
                                    .build();
                            carritoDetalleRepository.save(nuevoDetalle);
                        }
                );

        return buildCarritoResponse(carrito);
    }

    // ─── VER CARRITO ──────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarrito(Integer idUsuario) {
        Carrito carrito = carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoNoEncontradoException("CARRITO_NO_ENCONTRADO"));
        return buildCarritoResponse(carrito);
    }

    // ─── ACTUALIZAR CANTIDAD ──────────────────────────────────────────────────

    @Override
    @Transactional
    public CarritoResponseDTO actualizarCantidad(ActualizarCantidadRequestDTO request) {

        // 1. Obtener carrito del usuario (debe existir)
        Carrito carrito = carritoRepository.findByUsuarioIdUsuario(request.getIdUsuario())
                .orElseThrow(() -> new CarritoNoEncontradoException("CARRITO_NO_ENCONTRADO"));

        // 2. Buscar el detalle a actualizar
        CarritoDetalle detalle = carritoDetalleRepository
                .findByCarritoIdCarritoAndProductoIdProducto(carrito.getIdCarrito(), request.getIdProducto())
                .orElseThrow(() -> new ProductoNoEncontradoException("PRODUCTO_NO_EN_CARRITO"));

        // 3. Regla de negocio: cantidad = 0 → eliminar del carrito
        if (request.getCantidad() == 0) {
            carritoDetalleRepository.delete(detalle);
            return buildCarritoResponse(carrito);
        }

        // 4. Validar stock para la nueva cantidad
        Producto producto = detalle.getProducto();
        validarStock(producto, request.getCantidad());

        // 5. Actualizar
        detalle.setCantidad(request.getCantidad());
        carritoDetalleRepository.save(detalle);

        return buildCarritoResponse(carrito);
    }

    // ─── ELIMINAR PRODUCTO ────────────────────────────────────────────────────

    @Override
    @Transactional
    public CarritoResponseDTO eliminarProducto(Integer idUsuario, Integer idProducto) {

        Carrito carrito = carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoNoEncontradoException("CARRITO_NO_ENCONTRADO"));

        carritoDetalleRepository
                .deleteByCarritoIdCarritoAndProductoIdProducto(carrito.getIdCarrito(), idProducto);

        return buildCarritoResponse(carrito);
    }

    // ─── VACIAR CARRITO ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public CarritoResponseDTO vaciarCarrito(Integer idUsuario) {

        Carrito carrito = carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoNoEncontradoException("CARRITO_NO_ENCONTRADO"));

        carritoDetalleRepository.deleteByCarritoIdCarrito(carrito.getIdCarrito());

        return buildCarritoResponse(carrito);
    }

    // ─── HELPERS PRIVADOS ─────────────────────────────────────────────────────

    /**
     * Obtiene el carrito del usuario o lo crea si no existe.
     * Garantiza la regla: UN CARRITO POR USUARIO.
     */
    private Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseGet(() -> carritoRepository.save(
                        Carrito.builder()
                                .usuario(usuario)
                                .fechaCreacion(LocalDateTime.now())
                                .build()
                ));
    }

    /**
     * Valida que el producto tiene stock > 0 y que la cantidad solicitada
     * no supera el stock disponible.
     */
    private void validarStock(Producto producto, int cantidadSolicitada) {
        if (producto.getStock() == null || producto.getStock() <= 0) {
            throw new StockInsuficienteException("PRODUCTO_SIN_STOCK: " + producto.getNombre());
        }
        if (cantidadSolicitada > producto.getStock()) {
            throw new StockInsuficienteException(
                    "STOCK_INSUFICIENTE: disponible=" + producto.getStock()
                    + ", solicitado=" + cantidadSolicitada);
        }
    }

    /**
     * Construye el CarritoResponseDTO completo con ítems, subtotales y totales.
     */
    private CarritoResponseDTO buildCarritoResponse(Carrito carrito) {

        List<CarritoDetalle> detalles = carritoDetalleRepository
                .findByCarritoIdCarrito(carrito.getIdCarrito());

        List<CarritoDetalleResponseDTO> items = detalles.stream()
                .map(this::toDetalleResponse)
                .toList();

        int totalItems = items.stream()
                .mapToInt(CarritoDetalleResponseDTO::getCantidad)
                .sum();

        BigDecimal totalGeneral = items.stream()
                .map(CarritoDetalleResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CarritoResponseDTO.builder()
                .idCarrito(carrito.getIdCarrito())
                .idUsuario(carrito.getUsuario().getIdUsuario())
                .fechaCreacion(carrito.getFechaCreacion())
                .items(items)
                .totalItems(totalItems)
                .totalGeneral(totalGeneral)
                .build();
    }

    /**
     * Mapea un CarritoDetalle a su DTO de respuesta, calculando el subtotal.
     */
    private CarritoDetalleResponseDTO toDetalleResponse(CarritoDetalle detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return CarritoDetalleResponseDTO.builder()
                .idDetalle(detalle.getIdDetalle())
                .idProducto(detalle.getProducto().getIdProducto())
                .nombreProducto(detalle.getProducto().getNombre())
                .imagen(detalle.getProducto().getImagen())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
    }

    // ─── REALIZAR COMPRA ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public CompraResponseDTO realizarCompra(Integer idUsuario, ComprarRequestDTO request) {

        // 1. Obtener el carrito del usuario (debe existir)
        Carrito carrito = carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoNoEncontradoException("CARRITO_NO_ENCONTRADO"));

        // 2. Obtener los ítems del carrito
        List<CarritoDetalle> detalles = carritoDetalleRepository
                .findByCarritoIdCarrito(carrito.getIdCarrito());

        // 3. Validar que el carrito no esté vacío
        if (detalles.isEmpty()) {
            throw new CarritoVacioException("CARRITO_VACIO: no hay productos para confirmar la compra");
        }

        // 4. Calcular el total general
        BigDecimal total = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Obtener el estado CONFIRMADO (id_estado = 1 en tabla estados_pedido)
        EstadoPedido estadoConfirmado = estadoPedidoRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("ESTADO_PEDIDO_NO_CONFIGURADO: id_estado=1 no existe"));

        // 6. Crear y persistir el Pedido
        // Obtener el usuario de la base de datos para asegurar datos actualizados
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("USUARIO_NO_ENCONTRADO"));

        // Obtener dirección: request o del usuario
        String direccionEntrega = (request != null && request.getDireccionEntrega() != null
                && !request.getDireccionEntrega().isBlank())
                ? request.getDireccionEntrega()
                : usuario.getDireccion();

        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("El usuario no tiene una dirección registrada y no se proporcionó una dirección de entrega.");
        }

        // Validar y descontar stock antes de crear el pedido
        for (CarritoDetalle item : detalles) {
            Producto producto = item.getProducto();
            int stockDisponible = producto.getStock() != null ? producto.getStock() : 0;
            if (stockDisponible < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre() 
                        + ". Solicitado: " + item.getCantidad() + ", Disponible: " + stockDisponible);
            }
            // Descontar stock
            producto.setStock(stockDisponible - item.getCantidad());
            productoRepository.save(producto);
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .estadoPedido(estadoConfirmado)
                .fechaPedido(LocalDateTime.now())
                .direccionEntrega(direccionEntrega)
                .total(total)
                .build();
        pedidoRepository.save(pedido);

        // 7. Crear y persistir cada DetallePedido desde los ítems del carrito
        List<DetallePedido> detallesPedido = detalles.stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPrecioUnitario()
                            .multiply(BigDecimal.valueOf(item.getCantidad()));
                    return DetallePedido.builder()
                            .pedido(pedido)
                            .producto(item.getProducto())
                            .cantidad(item.getCantidad())
                            .precioUnitario(item.getPrecioUnitario())  // precio bloqueado desde el carrito
                            .subtotal(subtotal)
                            .build();
                })
                .toList();
        detallePedidoRepository.saveAll(detallesPedido);

        // 8. Vaciar el carrito después de confirmar la compra
        carritoDetalleRepository.deleteByCarritoIdCarrito(carrito.getIdCarrito());

        // 9. Construir y retornar el CompraResponseDTO
        List<CompraResponseDTO.DetallePedidoResponseDTO> detalleResponse = detallesPedido.stream()
                .map(dp -> CompraResponseDTO.DetallePedidoResponseDTO.builder()
                        .idProducto(dp.getProducto().getIdProducto())
                        .nombreProducto(dp.getProducto().getNombre())
                        .cantidad(dp.getCantidad())
                        .precioUnitario(dp.getPrecioUnitario())
                        .subtotal(dp.getSubtotal())
                        .build())
                .toList();

        return CompraResponseDTO.builder()
                .idPedido(pedido.getIdPedido())
                .idUsuario(idUsuario)
                .estado(estadoConfirmado.getNombre())
                .fechaCompra(pedido.getFechaPedido())
                .direccionEntrega(pedido.getDireccionEntrega())
                .detalle(detalleResponse)
                .total(total)
                .build();
    }
}
