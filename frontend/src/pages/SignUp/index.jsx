import axios from "axios";
import { useEffect, useMemo, useState } from "react";
import { useTranslation } from 'react-i18next';  // Bu satırı ekleyin
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/style.css";
import { signUp } from "./api";
import { Input } from "./components/Input";
import { LanguageSelector } from "../../shared/components/LanguageSelector";

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
  const { t } = useTranslation(); 

  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        firstName: undefined,
      };
    });
  }, [firstName]);
  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        firstName: undefined,
      };
    });
  }, [firstName]);
  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        lastName: undefined,
      };
    });
  }, [lastName]);
  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        phoneNumber: undefined,
      };
    });
  }, [phoneNumber]);
  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        username: undefined,
      };
    });
  }, [username]);
  useEffect(() => {
    setErrors(function(lasterErrors) {
      return {
        ...lasterErrors,
        password: undefined,
      };
    });
  }, [password]);




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
  const passwordRepeatError = useMemo(() => {
    if( password && password!== passwordRepeat){
      return t('passwordMismatch');
    }
    return '';
  },[password, passwordRepeat]);
  
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
          <h3 className="m-0">{t('signUp')}</h3>
        </div>
        <div className="card-body">
          <form onSubmit={onSubmit}>
            <Input
              id="firstName"
              label={t('firstName')}
              value={firstName}
              onChange={(event) => setFirstName(event.target.value)}
              error={errors.firstName}
            />
            <Input
              id="lastName"
              label={t('lastName')}
              value={lastName}
              onChange={(event) => setLastName(event.target.value)}
              error={errors.lastName}
            />
            <div className="mb-3">
              <label htmlFor="phoneNumber" className="form-label">
                {t('phoneNumber')}
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
            <Input
              id="username"
              label={t('username')}
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              error={errors.username}
            />
            <Input
              id="email"
              label={t('email')}
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              error={errors.email}
            />
            <Input
              id="password"
              label={t('password')}
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              error={errors.password}
            />
            <Input
              id="passwordRepeat"
              label={t('passwordRepeat')}
              type="password"
              value={passwordRepeat}
              onChange={(event) => setPasswordRepeat(event.target.value)}
              error={passwordRepeatError}
            />
            <div>
              {successMessage && (
                <div className="alert alert-success">{successMessage}</div>
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
              {t('signUp')} 
            </button>
          </form>
        </div>
        <LanguageSelector />
      </div>
    </div>
  );
}
