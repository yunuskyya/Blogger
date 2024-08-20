import axios from "axios";
import { useEffect, useState } from "react";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/style.css";
import { signUp } from "./api";

export function SignUp() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordRepeat, setPasswordRepeat] = useState("");
  const [apiProgress, setApiProgress] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errors, setErrors] = useState({});
  const [passwordMatchError, setPasswordMatchError] = useState(false);

  useEffect(() => {
    setErrors({});
    setPasswordMatchError(false);
  }, [firstName, lastName, phoneNumber, username, email, password, passwordRepeat]);

  const onSubmit = async (event) => {
    event.preventDefault();
    setSuccessMessage("");
    setApiProgress(true);

    if (password !== passwordRepeat) {
      setPasswordMatchError(true);
      setApiProgress(false);
      return;
    }

    try {
      const response = await signUp({
        firstName,
        lastName,
        phoneNumber,
        username,
        email,
        password,
      });
      setSuccessMessage(response.data.message);
    } catch (axiosError) {
      if (
        axiosError.response?.data &&
        axiosError.response.data.status === 400
      ) {
        setErrors(axiosError.response.data.validationErrors);
      }
    } finally {
      setApiProgress(false);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div
        className="card shadow-lg p-4"
        style={{ width: "100%", maxWidth: "500px" }}
      >
        <div
          className="card-header text-center mb-4"
          style={{ backgroundColor: "#f7f7f7", borderRadius: "10px" }}
        >
          <h3 className="m-0">Sign Up</h3>
        </div>
        <div className="card-body">
          <form onSubmit={onSubmit}>
            <div className="mb-3">
              <label htmlFor="firstName" className="form-label">
                First Name
              </label>
              <input
                id="firstName"
                className={
                  errors.firstName ? "form-control is-invalid" : "form-control"
                }
                onChange={(event) => setFirstName(event.target.value)}
                required
              />
              <div className="invalid-feedback">{errors.firstName}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="lastName" className="form-label">
                Last Name
              </label>
              <input
                id="lastName"
                className={
                  errors.lastName ? "form-control is-invalid" : "form-control"
                }
                onChange={(event) => setLastName(event.target.value)}
                required
              />
              <div className="invalid-feedback">{errors.lastName}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="phoneNumber" className="form-label">
                Phone Number
              </label>
              <PhoneInput
                country={"tr"}
                value={phoneNumber}
                onChange={(phone) => setPhoneNumber(phone)}
                inputStyle={{ width: "100%" }}
                containerStyle={{ width: "100%" }}
                inputProps={{
                  name: "phone",
                  required: true,
                  className: errors.phoneNumber
                    ? "form-control is-invalid"
                    : "form-control",
                }}
              />
              <div className="invalid-feedback">{errors.phoneNumber}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                Username
              </label>
              <input
                id="username"
                className={
                  errors.username ? "form-control is-invalid" : "form-control"
                }
                onChange={(event) => setUsername(event.target.value)}
                required
              />
              <div className="invalid-feedback">{errors.username}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                id="email"
                className={
                  errors.email ? "form-control is-invalid" : "form-control"
                }
                type="email"
                onChange={(event) => setEmail(event.target.value)}
                required
              />
              <div className="invalid-feedback">{errors.email}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                id="password"
                className={
                  errors.password ? "form-control is-invalid" : "form-control"
                }
                type="password"
                onChange={(event) => setPassword(event.target.value)}
                required
              />
              <div className="invalid-feedback">{errors.password}</div>
            </div>
            <div className="mb-3">
              <label htmlFor="passwordRepeat" className="form-label">
                Repeat Password
              </label>
              <input
                id="passwordRepeat"
                className={
                  passwordMatchError ? "form-control is-invalid" : "form-control"
                }
                type="password"
                onChange={(event) => setPasswordRepeat(event.target.value)}
                required
              />
              <div className="invalid-feedback">
                {passwordMatchError && "Passwords do not match"}
              </div>
            </div>
            <div>
              {successMessage && (
                <div className="alert alert-success"> {successMessage}</div>
              )}
            </div>
            <button
              className="btn btn-primary w-100"
              type="submit"
              disabled={apiProgress || !password || password !== passwordRepeat}
            >
              {apiProgress && (
                <span
                  className="spinner-border spinner-border-sm"
                  aria-hidden="true"
                ></span>
              )}
              Sign Up
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
