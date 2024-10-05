/**
 * 
 */
package projects2.exception;

/**
 * 
 */
public class DbException2 extends RuntimeException {

	/**
	 * 
	 */
	public DbException2() {
	}

	/**
	 * @param message
	 */
	public DbException2(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DbException2(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DbException2(String message, Throwable cause) {
		super(message, cause);
	}
}
