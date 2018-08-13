var client = axios.create({
  baseURL: '',
  timeout: 2500,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  transformRequest: function (data) {
    let ret = ''
    for (let it in data) {
      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
    }
    return ret
  }
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