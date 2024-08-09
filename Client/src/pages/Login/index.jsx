import styles from './style.module.scss';

function Login() {
  return (
    <div className={styles.login}>
      <div className={styles.content}>
        <h1>Login</h1>
        <form>
          <div className={styles.inputBox}>
            <input
              type="text"
              placeholder="Email"
              name="email"
              required
            />
            <input
              type="password"
              placeholder="Password"
              name="password"
              required
            />
          </div>
          <div className={styles.links}>
            <p className={styles.forgotPassword}>Forgot Password</p>
            <p className={styles.createAccount}>Create Account</p>
          </div>
          <button className={styles.loginButton}>Login</button>
        </form>
      </div>
    </div>
  );
}

export default Login;