import { useState } from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<h1>Hello Sumaniac !!</h1>} />
        </Routes>
      </Router>

    </>
  )
}

export default App
