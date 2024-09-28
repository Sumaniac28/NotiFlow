import styles from './style.module.scss';
import Navbar from '../../components/Navbar';

function About() {
    return(
        <div>
            <div className={styles.about}>
                <h1>Welcome to About Page</h1>   
            </div>
        </div>
    );
}

export default About;