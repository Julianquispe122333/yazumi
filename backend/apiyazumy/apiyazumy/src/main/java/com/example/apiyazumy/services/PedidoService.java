package com.example.apiyazumy.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.CarritoDetalle;
import com.example.apiyazumy.entities.DetallePedido;
import com.example.apiyazumy.entities.EstadoPedido;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.entities.Producto;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.repositories.DetallePedidoRepository;
import com.example.apiyazumy.repositories.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoDetalleService carritoDetalleService;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @Autowired
    private ProductoService productoService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> listarPorUsuario(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        return pedidoRepository.findByUsuario(usuario);
    }

    public List<Pedido> listarPorEstado(Integer idEstado) {
        EstadoPedido estado = estadoPedidoService.obtenerPorId(idEstado);
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional
    public Pedido crearPedidoDesdeCarrito(Integer idUsuario, String direccionEntrega, String observaciones) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        Carrito carrito = carritoService.obtenerPorIdUsuario(idUsuario);
        List<CarritoDetalle> detallesCarrito = carritoDetalleService.obtenerPorCarrito(carrito.getIdCarrito());

        if (detallesCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        EstadoPedido estadoInicial = estadoPedidoService.obtenerPorNombre("Registrado");

        Pedido pedido = new Pedido(usuario, estadoInicial, direccionEntrega, observaciones);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.ZERO);
        pedido = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;
        for (CarritoDetalle detalleCarrito : detallesCarrito) {
            Producto producto = detalleCarrito.getProducto();
            Integer cantidad = detalleCarrito.getCantidad();
            // Descontar stock
            productoService.descontarStock(producto.getIdProducto(), cantidad);

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetallePedido detallePedido = new DetallePedido(pedido, producto, cantidad, precioUnitario);
            detallePedido.setSubtotal(subtotal); // Aunque el constructor ya lo calcula, lo dejamos explícito
            detallePedidoRepository.save(detallePedido);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        // Vaciar carrito
        carritoDetalleService.vaciarCarrito(carrito.getIdCarrito());
        return pedido;
    }

    @Transactional
    public Pedido cambiarEstado(Integer idPedido, Integer idNuevoEstado, String comentario) {
        Pedido pedido = obtenerPorId(idPedido);
        EstadoPedido nuevoEstado = estadoPedidoService.obtenerPorId(idNuevoEstado);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelarPedido(Integer idPedido) {
        Pedido pedido = obtenerPorId(idPedido);
        EstadoPedido cancelado = estadoPedidoService.obtenerPorNombre("Cancelado");
        pedido.setEstado(cancelado);
        // Reponer stock
        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
        for (DetallePedido detalle : detalles) {
            productoService.reponerStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }
        return pedidoRepository.save(pedido);
    }
}