package com.example.apiyazumy.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.CarritoDetalle;
import com.example.apiyazumy.entities.DetallePedido;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.entities.Producto;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.services.CarritoDetalleService;
import com.example.apiyazumy.services.CarritoService;
import com.example.apiyazumy.services.DetallePedidoService;
import com.example.apiyazumy.services.PedidoService;
import com.example.apiyazumy.services.ProductoService;
import com.example.apiyazumy.services.UsuarioService;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoDetalleService carritoDetalleService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Integer idUsuario, @RequestBody Usuario datos) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(idUsuario, datos);
            actualizado.setPasswordHash(null);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/productos")
    public List<Producto> listarProductos() {
        return productoService.listarActivos();
    }

    @GetMapping("/productos/{idProducto}")
    public Producto obtenerProducto(@PathVariable Integer idProducto) {
        return productoService.obtenerPorId(idProducto);
    }

    @GetMapping("/{idUsuario}/carrito")
    public Carrito obtenerCarrito(@PathVariable Integer idUsuario) {
        return carritoService.obtenerPorIdUsuario(idUsuario);
    }

    @PostMapping("/{idUsuario}/carrito/agregar")
    public ResponseEntity<?> agregarProducto(@PathVariable Integer idUsuario,
                                             @RequestParam Integer idProducto,
                                             @RequestParam Integer cantidad) {
        try {
            Carrito carrito = carritoService.obtenerPorIdUsuario(idUsuario);
            CarritoDetalle detalle = carritoDetalleService.agregarProducto(carrito.getIdCarrito(), idProducto, cantidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/carrito/detalle/{idDetalle}")
    public ResponseEntity<?> modificarCantidad(@PathVariable Integer idDetalle,
                                               @RequestParam Integer nuevaCantidad) {
        try {
            CarritoDetalle detalle = carritoDetalleService.actualizarCantidad(idDetalle, nuevaCantidad);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/carrito/detalle/{idDetalle}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer idDetalle) {
        carritoDetalleService.eliminarProducto(idDetalle);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}/carrito/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Integer idUsuario) {
        Carrito carrito = carritoService.obtenerPorIdUsuario(idUsuario);
        carritoDetalleService.vaciarCarrito(carrito.getIdCarrito());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idUsuario}/pedido")
    public ResponseEntity<?> crearPedido(@PathVariable Integer idUsuario,
                                         @RequestParam String direccionEntrega,
                                         @RequestParam(required = false) String observaciones) {
        try {
            Pedido pedido = pedidoService.crearPedidoDesdeCarrito(idUsuario, direccionEntrega, observaciones);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{idUsuario}/pedidos")
    public List<Pedido> historialPedidos(@PathVariable Integer idUsuario) {
        return pedidoService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/pedidos/{idPedido}")
    public Pedido obtenerPedido(@PathVariable Integer idPedido) {
        return pedidoService.obtenerPorId(idPedido);
    }

    @GetMapping("/pedidos/{idPedido}/detalles")
    public List<DetallePedido> obtenerDetallesPedido(@PathVariable Integer idPedido) {
        return detallePedidoService.obtenerPorPedido(idPedido);
    }
}