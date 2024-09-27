import styles from './style.module.scss';
import Navbar from '../../components/Navbar';
import Demo from '../../assets/eyesDemo.jpg';
import { Link } from 'react-router-dom';

export default function Home(){
    return(
        <div>
            <div className={styles.home}>
                <div className={styles.text}>
                    <h1>Start Managing with NotiFlow API</h1>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et</p>
                    <div className={styles.buttonsDiv}>
                        <Link to="/docs"><button class={styles.but}>Get Started</button></Link>
                        <Link to="/about"><button class={styles.but}>Learn More</button></Link>
                    </div>
                </div>
                <div className={styles.heroImage}>
                    <img className={styles.pfp} src={Demo} alt="image" />
                </div>
            </div>
        </div>
    );
}