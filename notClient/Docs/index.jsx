import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import styles from './style.module.scss';
import Sidebar from '../../Client/src/components/Sidebar'
import Intro from '../../Client/src/components/DocsComponents/Intro' 


function Docs() {
    return(
        <div>
            <Sidebar />
            <div className={styles.docs}>
                Docs
            </div>
        </div>
    );
}

export default Docs;