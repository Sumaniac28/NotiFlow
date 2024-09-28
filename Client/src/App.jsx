import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './pages/Home';
import DocsLayout from './pages/DocsLayout';
import About from './pages/About';
import Contact from './pages/Contact';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import Layout from './components/Layout';
import Intro from './components/DocsComponents/Intro';
import GuidesInfo from './components/DocsComponents/GuidesInfo';
import ApiInfo from './components/DocsComponents/ApiInfo';
import ExamplesInfo from './components/DocsComponents/ExamplesInfo';
import SdkInfo from './components/DocsComponents/SdkInfo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="docs" element={<DocsLayout />}>
            <Route index element={<Intro />} />
            <Route path="guides" element={<GuidesInfo />} />
            <Route path="api" element={<ApiInfo />} />
            <Route path="examples" element={<ExamplesInfo />} />
            <Route path="sdk" element={<SdkInfo />} />
          </Route>
          <Route path="about" element={<About />} />
          <Route path="contact" element={<Contact />} />
        </Route>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />

      </Routes>
    </Router>
  )
}

export default App
