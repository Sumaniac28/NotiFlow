import styles from './style.module.scss'

export default function SignUp() {
  return (
    <div className={styles.signUp}>
      <div className={styles.content}>
        <h1>Sign Up</h1>
        <form>
          <div className={styles.inputBox}>
            <input
                type="text"
                placeholder="Name"
                name="name"
                required
            />
            <input
              type="text"
              placeholder="Email"
              name="email"
              required
            />
            <input
              type="number"
              placeholder="Phone No"
              name="phone"
              required
            />
            <input
              type="password"
              placeholder="Password"
              name="password"
              required
            />
            <input
              type="password"
              placeholder="Confirm Password"
              name="confirmPassword"
              required
            />
          </div>
          <div className={styles.links}>
            <p className={styles.forgotPassword}>  </p>
            <p className={styles.createAccount}>Have an Account</p>
          </div>
          <button className={styles.signUpButton}>Sign Up</button>
        </form>
      </div>
    </div>
  );
}
  
 