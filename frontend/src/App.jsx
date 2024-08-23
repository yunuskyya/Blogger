import { Outlet } from "react-router-dom";
import { NavBar } from "./shared/components/Navbar";

function App() {
  return (
    <>
      <NavBar />
      <div className="container mt-3">
        <Outlet />
      </div>
    </>
  );
}

export default App;
