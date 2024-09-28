import styles from './style.module.scss';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';

function Sidebar() {
    const nav = useNavigate();
    const [selected, setSelected] = useState();
    
    useEffect(() => {
        setSelected(location.pathname);
    }, [location.pathname]);

    const handleNavigation = (path) => {
        nav(path)
        setSelected(path)
    }

    return(
        <div className={styles.wrapper}>
            <div className={styles.sidebar}>
                <p className={selected === "/docs" ? styles.selected : ""} onClick={()=>handleNavigation("/docs")}>Introduction</p>
                <p className={selected.startsWith("/docs/guides") ? styles.selected : ""} onClick={()=>handleNavigation("/docs/guides")}>Guides</p>
                <p className={selected.startsWith("/docs/api") ? styles.selected : ""} onClick={()=>handleNavigation("/docs/api")}>API</p>
                <p className={selected.startsWith("/docs/examples") ? styles.selected : ""} onClick={()=>handleNavigation("/docs/examples")}>Examples</p>
                <p className={selected === "/docs/sdk" ? styles.selected : ""} onClick={()=>handleNavigation("/docs/sdk")}>SDK's</p>
                <p>About</p>
            </div>
        </div>
    );
}
export default Sidebar;