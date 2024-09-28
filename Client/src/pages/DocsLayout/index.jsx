import styles from './style.module.scss';
import Sidebar from '../../components/Sidebar'
import {Outlet} from 'react-router-dom';

export default function DocsLayout() {
    return(
        <div>
            <Sidebar />
            <div className={styles.docs}>
                <Outlet />
            </div>
        </div>
    );
}