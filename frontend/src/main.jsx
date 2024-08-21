import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { SignUp } from "./pages/SignUp/index.jsx";
import "./locales";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <SignUp />
  </StrictMode>
);
