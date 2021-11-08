import axios from 'axios';

const BASE_URL = ''

function registerNewUser(email, password) {
    const url = `${BASE_URL}/new-user`
    const payload = {
        email: email,
        password: password
    }

    return axios.post(url, payload)
        .then(() => true)
        .catch(_ => {
            return false;
        });
}

function loginUser(email, password) {
    const url = `${BASE_URL}/login`
    const params = {
        email: email,
        password: password
    }

    return axios.post(url, null,{params: params})
        .then(response => {
            if (response.status === 204) {
                return 'success';
            } else if (response.status === 401) {
                return 'wrong_credentials';
            } else {
                return 'failure'
            }
        })
        .catch(error => {
            if (error.response.status === 401) {
                return 'wrong_credentials';
            } else {
                return 'failure';
            }
        });
}

function getUserDetails() {
    const url = `${BASE_URL}/user-details`

    return axios.get(url).then(response => response.data);
}

function logout() {
    const url = `${BASE_URL}/logout`

    return axios.post(url);
}

export {getUserDetails, loginUser, logout, registerNewUser, BASE_URL};