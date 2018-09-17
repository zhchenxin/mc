var client = axios.create({
  baseURL: 'http://localhost:8080/',
  timeout: 60000,
});

client.interceptors.response.use(function (response) {
  if (response.data.code === 0) {
    return response
  } else {
    return Promise.reject(response.data.msg)
  }
}, function (error) {
  return Promise.reject(error.message)
});