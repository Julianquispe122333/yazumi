package mobile;

import java.util.Scanner;

public class ogin {
    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)){
            String usuarioCorrecto = "Rusberth";
            String claveCorrecta = "123456";
            System.out.print("Usuario: ");
            String usuario = teclado.nextLine();
            System.out.print("Contraseña: ");
            String clave = teclado.nextLine();
            if (usuario.equals(usuarioCorrecto) && clave.equals(claveCorrecta)) {
                System.out.println(" Login exitoso");
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        }
    }
}