import java.math.BigInteger;
import java.util.ArrayList;

public class RSA {
	
	// M�todo para obtener el m�ximo com�n divisor (Algoritmo euclideano b�sico)
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (a.compareTo(BigInteger.valueOf(0)) == 0) {
			return b;
		} else {
			return gcd(b.mod(a), a);
		}
	}

	// M�todo para obtener el m�ximo com�n divisor (Algoritmo euclideano extendido)
	// ax + by = gcd(a, b)
	public static Tuple3 gcdExtended(BigInteger a, BigInteger b) {
		if (a.compareTo(BigInteger.valueOf(0)) == 0) {
			return new Tuple3(b, BigInteger.valueOf(0), BigInteger.valueOf(1));
		} else {
			Tuple3 gcd = gcdExtended(b.mod(a), a);
			return new Tuple3(gcd.getGCD(), gcd.getY().subtract(b.divide(a).multiply(gcd.getX())), gcd.getX());
		}
	}

	public static ArrayList<BigInteger> encriptar(String mensaje, BigInteger e, BigInteger n) {
		ArrayList<BigInteger> resultado = new ArrayList<BigInteger>();

		// (ch^e) mod n
		for (Character ch : mensaje.toCharArray()) {
			BigInteger base = BigInteger.valueOf((int) ch);
			BigInteger exponent = e;
			BigInteger mod = n;
			BigInteger c = base.modPow(exponent, mod);

			resultado.add(c);
		}
		return resultado;
	}

	public static String desencriptar(ArrayList<BigInteger> mensaje, BigInteger d, BigInteger n) {
		String resultado = "";
		// (ch^d) mod n
		// Nota: ch ya est� encriptado
		for (BigInteger num : mensaje) {
			BigInteger base = num;
			BigInteger exponent = d;
			BigInteger mod = n;
			BigInteger c = base.modPow(exponent, mod);

			char character = (char) (c.intValue());
			resultado += character;
		}
		return resultado;
	}

	public static void main(String[] args) {
		// p y q son n�meros primos
		BigInteger p = BigInteger.valueOf(1993), q = BigInteger.valueOf(1997);

		// Primera parte por hacer para obtener la llave p�blica
		// n ser� el m�dulo a usar para encriptar y desencriptar mensajes
		// n = p * q
		BigInteger n = p.multiply(q);

		// Calcular phi que es la segunda parte para obtener la llave p�blica
		// "e" ser� la llave p�blica y sufrir� cambios al encontrar el coprimo
		// phi = (p - 1) * (q - 1)
		BigInteger phi = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
		BigInteger e = BigInteger.valueOf(2);

		// "e" tienes que ser menor y coprimo de phi
		// e < phi
		// -1 less, 0 equal, 1 greater
		while (e.compareTo(phi) == -1) {
			if (gcd(e, phi).compareTo(BigInteger.valueOf(1)) == 0) {
				break;
			} else {
				e = e.add(BigInteger.valueOf(1));
			}
		}

		// Obtenci�n de la llave privada
		BigInteger d = gcdExtended(e, phi).getX().mod(phi);
		String msg = "hola soy rey";

		ArrayList<BigInteger> msgEncriptado = encriptar(msg, e, n);
		System.out.println(msgEncriptado);
		System.out.println(desencriptar(msgEncriptado, d, n));
	}
}