import Login from "./components/Login"
import Authorize from "./components/Authorize"
import { BrowserRouter, Routes, Route } from "react-router-dom";

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/authorize" element={<Authorize/>}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App
