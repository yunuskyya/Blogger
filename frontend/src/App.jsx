import { Link, Outlet } from "react-router-dom";
import logo from "./assets/ad.png";
import { LanguageSelector } from "./shared/components/LanguageSelector";
import { useTranslation } from "react-i18next";

function App() {
  const { t } = useTranslation();
  return (
    <>
      <nav className="navbar navbar-expand-lg bg-body-tertiary shadow-sm">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">
            <img
              src={logo}
              alt="Blog Logo"
              style={{ width: "50px", height: "auto" }}
            />
            Blogger
          </Link>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto">
              <li className="nav-item">
                <Link className="nav-link" to="/signup">
                  {t('signUp')}
                </Link>
              </li>
              <li className="nav-item d-flex align-items-center">
                <LanguageSelector />
              </li>
            </ul>
          </div>
        </div>
      </nav>
      <div className="container mt-3">
        <Outlet />
      </div>
    </>
  );
}

export default App;
