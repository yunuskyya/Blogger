import axios from 'axios';

export function login({ identifier, password }) {
  let payload = {};

  if (identifier.includes('@')) {
    payload.email = identifier;
  } 
  else if (/^\d+$/.test(identifier)) {
    payload.phoneNumber = identifier;
  } 
  else {
    payload.username = identifier;
  }

  payload.password = password;

  return axios.post('/api/v1/auth', payload, { withCredentials: true });
}

export function logout() {
  return axios.delete('/api/v1/auth', { withCredentials: true });
}
