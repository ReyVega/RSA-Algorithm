import java.math.BigInteger;
import java.util.ArrayList;

public class RSA {
	
	// Método para obtener el máximo común divisor (Algoritmo euclideano básico)
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (a.compareTo(BigInteger.valueOf(0)) == 0) {
			return b;
		} else {
			return gcd(b.mod(a), a);
		}
	}

	// Método para obtener el máximo común divisor (Algoritmo euclideano extendido)
	// ax + by = gcd(a, b)
	public static Tuple3 gcdExtended(BigInteger a, BigInteger b) {
		if (a.compareTo(BigInteger.valueOf(0)) == 0) {
			return new Tuple3(b, BigInteger.valueOf(0), BigInteger.valueOf(1));
		} else {
			Tuple3 gcd = gcdExtended(b.mod(a), a);
			return new Tuple3(gcd.getGCD(), gcd.getY().subtract(b.divide(a).multiply(gcd.getX())), gcd.getX());
		}
	}
	
	// Exponenciación rápida
	public static BigInteger fastExponentiation(BigInteger a, BigInteger b, BigInteger m) {
		BigInteger result = BigInteger.valueOf(1);
		// while(b > 0)
		while(b.compareTo(BigInteger.valueOf(0)) == 1) {
			// if(b % 2 == 0)
			if(b.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(1)) == 0) {
				result = result.multiply(a).mod(m);
			}
			a = a.multiply(a).mod(m);
			b = b.divide(BigInteger.valueOf(2));
		}
		return result;
	}

	public static ArrayList<BigInteger> encriptar(String mensaje, BigInteger e, BigInteger n) {
		ArrayList<BigInteger> resultado = new ArrayList<BigInteger>();

		// (ch^e) mod n
		for (Character ch : mensaje.toCharArray()) {
			BigInteger base = BigInteger.valueOf((int) ch);
			BigInteger exponent = e;
			BigInteger mod = n;
			BigInteger c = fastExponentiation(base,exponent,mod);

			resultado.add(c);
		}
		return resultado;
	}

	public static String desencriptar(ArrayList<BigInteger> mensaje, BigInteger d, BigInteger n) {
		String resultado = "";
		// (ch^d) mod n
		// Nota: ch ya está encriptado
		for (BigInteger num : mensaje) {
			BigInteger base = num;
			BigInteger exponent = d;
			BigInteger mod = n;
			BigInteger c = fastExponentiation(base,exponent,mod);

			char character = (char) (c.intValue());
			resultado += character;
		}
		return resultado;
	}

	public static void main(String[] args) {
		// p y q son números primos
		BigInteger p = BigInteger.valueOf(1993), q = BigInteger.valueOf(1997);

		// Primera parte por hacer para obtener la llave pública
		// n será el módulo a usar para encriptar y desencriptar mensajes
		// n = p * q
		BigInteger n = p.multiply(q);

		// Calcular phi que es la segunda parte para obtener la llave pública
		// "e" será la llave pública y sufrirá cambios al encontrar el coprimo
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

		// Obtención de la llave privada
		BigInteger d = gcdExtended(e, phi).getX().mod(phi);
		String msg = "hola soy rey";

		ArrayList<BigInteger> msgEncriptado = encriptar(msg, e, n);
		System.out.println(msgEncriptado);
		System.out.println(desencriptar(msgEncriptado, d, n));
	}
}