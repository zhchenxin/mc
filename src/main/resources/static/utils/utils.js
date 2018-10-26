var client = axios.create({
  timeout: 15000,
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

// 数组去重方法
function unique(array) {
   return Array.from(new Set(array));
}