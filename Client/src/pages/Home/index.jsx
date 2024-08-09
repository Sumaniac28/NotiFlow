import styles from './style.module.scss';
import demo from '../../assets/demo.jpg'
export default function Home(){
    return(
        <div className={styles.home}>
            <div className={styles.text}>
                <h1>Start Managing with NotiFlow API</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et</p>
            </div>
            <div className={styles.heroImage}>
                <img className={styles.pfp} src={demo} alt="image" />
            </div>
        </div>
    );
}