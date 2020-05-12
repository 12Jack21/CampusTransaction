import Request from '@/utils/luch-request/index.js'

const http = new Request()

// 静态全局配置
http.setConfig((config) => { /* config 为默认全局配置*/
		console.log('http default config',config);
    // config.baseUrl = 'http://localhost:9999'; /* 根域名 */
		config.baseUrl = 'https://easy-mock.com/mock/5eb89db640d5e5498f113eff/ct'
    config.header = {
        a: 1, // 演示用
    }
		config.responseType = 'json'
    return config
})

// 拦截器，请求前拦截
http.interceptor.request((config, cancel) => { /* cancel 为函数，如果调用会取消本次请求。需要注意：调用cancel,本次请求的catch仍会执行。必须return config */
    config.header = {
      ...config.header,
      token: uni.getStorageSync('token') || '' // 演示拦截器header加参
    }
    // 演示custom 用处
    // if (config.custom.auth) {
    //   config.header.token = 'token'
    // }
    /**
    /* 演示cancel 函数
    if (!token) { // 如果token不存在，调用cancel 会取消本次请求，不会进入响应拦截器，但是该函数的catch() 仍会执行
      cancel('token 不存在', config) //  把修改后的config传入，之后响应就可以拿到修改后的config。 
			如果调用了cancel但是不传修改后的config，则catch((err) => {}) err.config 为request拦截器修改之前的config
    }
    **/
    return config
  })

// 响应后拦截
http.interceptor.response((response) => { /* 对响应成功做点什么 （statusCode === 200），必须return response*/
  //  if (response.data.code !== 200) { // 服务端返回的状态码不等于200，则reject()
  //    return Promise.reject(response) // return Promise.reject 可使promise状态进入catch
 // if (response.config.custom.verification) { // 演示自定义参数的作用
  //   return response.data
  // }
  console.log('response',response)
  return response
}, (response) => { /*  对响应错误做点什么 （statusCode !== 200），必须return response*/
  console.log('error',response)
  return response
})

export default{
	testAPI(config){
		return http.get('/test',{params:{outdated:'',age:12,op:''}})
	},
	uploadImage(filePath,config){
		return http.upload('/commodities/image',{
			filePath,
			name: 'image',
			...config
		})
	},
	addNotice(data){
		return http.post('/notice/add',{
			data
		})
	},
	getSearchHistory(id){
		return http.get('/histories/account/' + id)
	},
	delSearchHistory(ids){
		return http.delete('/histories',{ids})
	},
	clearSearchHistory(id){
		return http.delete('/histories/account/' + id)
	},
	getCommodity(id){
		return http.get('/commodities/' + id)
	},
	getSearchResult(keyword, condition, pagination){ 
		return http.post('/commodities/search',{keyword, condition, pagination})
	},
	getCommodities(sort, pagination){ //add last commodity id
		return http.get('/commodities/sort/' + sort, {params: pagination})
	}
}