import * as Icon from 'react-bootstrap-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import styles from './style.module.scss'
import { useState, useEffect } from 'react';

export default function Navbar(){
    const nav = useNavigate()
    const location = useLocation()
    const [selected, setSelected] = useState(location.pathname)

    useEffect(() => {
        setSelected(location.pathname);
    }, [location.pathname]);

    const handleNavigation = (path) => {
        nav(path)
        setSelected(path)
    }

    return(
        <div className={styles.wrapper}>
            <div className={styles.navbar}>
                <p className={selected === "/" ? styles.selected : ""} onClick={() => handleNavigation("/")}>Home</p>
                <p className={selected.startsWith("/docs") ? styles.selected : ""} onClick={() => handleNavigation("/docs")}>Documentation</p>
                <p className={selected === "/about" ? styles.selected : ""} onClick={() => handleNavigation("/about")}>About</p>
                <p className={selected === "/contact" ? styles.selected : ""} onClick={() => handleNavigation("/contact")}>Contact</p>
                <p className={selected === "/login" ? styles.selected : ""} onClick={() => handleNavigation("/login")}>Login</p>
                <p className={selected === "/signup" ? styles.selected : ""} onClick={() => handleNavigation("/signup")}>Sign Up</p>
            </div>
        </div>
    );
}