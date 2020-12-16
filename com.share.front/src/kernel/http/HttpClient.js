
import func from './func';
import Taro from '@tarojs/taro'

//ajax调用core
const requestWithHook = (method, api, data, header,responseHook) => {
    return new Promise((resolve, reject) => {
        
        const req = (resolve, reject) => {
            Taro.request({
                method: method,
                url: api,
                data: data,
                header:header
            }).then(res => {
                let response = responseHook(res.data);
                if(response.isSuccess){
                    resolve(response)
                }else{
                    reject(response)
                }
            }).catch(res => {
                res.json().then(r=>{
                    console.log(r)
                    let response = responseHook(r);
                    reject(response)
                }).catch(err=>{
                    console.log(err)
                    let response = responseHook(err);
                    reject(response)
                })
            })
        };
        //出现异常后2s后重试，共重试2次
        func.tryCall(req, 2000, 1)(resolve, reject)
    })
};

//ajax调用core
const _request = ({method, api, data, header,responseHook},done) => {
    return new Promise((resolve, reject) => {
        const req = (resolve, reject) => {
            Taro.request({
                method: method,
                url: api,
                data: data,
                header:header
            }).then(res => {
                let response = responseHook(res.data);
                if(response.isSuccess){
                    if (typeof done === 'function'){
                        done(response)
                    }
                    resolve(response)
                }else{
                    reject(response)
                }
            }).catch(res => {
                let response = responseHook(res.data);
                reject(response)
            })
        };
        //出现异常后2s后重试，共重试2次
        func.tryCall(req, 2000, 1)(resolve, reject)
    })
};

//ajax调用core
const request = (method, api, data, header) => {
    return new Promise((resolve, reject) => {
        const req = (resolve, reject) => {
            Taro.request({
                method: method,
                url: api,
                data: data,
                header:header
            }).then(res => {
                // console.log(res);
                // resolve(res.data)
                if(Protocol.isLegal(res.data)){
                    let response = Protocol.deserialize(res.data);
                    if(response.isSuccess()){
                        resolve(response)
                    }else{
                        // console.log(res);
                        let resData = []
                        resData.code = response.code
                        resData.message = response.getErrorMsg()
                        resData.response = res.data
                        reject(resData)
                    }
                }else{
                    resolve(res.data)
                }

            }).catch(res => {
                if(Protocol.isLegal(res.data)){
                    let response = Protocol.deserialize();
                    // console.log(res);
                    let resData = []
                    resData.code = response.code
                    resData.message = response.getErrorMsg()
                    resData.response = res.data
                    reject(resData)
                }else{
                    reject(res.data)
                }
            })
        };
        //出现异常后2s后重试，共重试2次
        func.tryCall(req, 2000, 1)(resolve, reject)
    })
};

//ajax调用core
const requestCommon = (params,responseHook) => {
    return new Promise((resolve, reject) => {
        const req = (resolve, reject) => {
            Taro.request(params).then(res => {
                let response = responseHook(res.data);
                resolve(response)
            }).catch(res => {
                let response = responseHook(res.data);
                reject(response)
            })
        };
        //出现异常后2s后重试，共重试2次
        func.tryCall(req, 2000, 1)(resolve, reject)
    })
};


//缓存调用后的结果60分钟
const cacheCall = func.cacheCall((args, resolve) => {
    request(args.method, args.api, args.data).then(resolve)
}, 60 * 60 * 1000);
//缓存调用封装为Promise
const cacheRequest = (method, api, data) => {
    return new Promise((resolve, reject) => {
        cacheCall({ method, api, data }, resolve)
    })
};
//get请求
const get = (api, data,header) => {
    return request('GET', api, data,header)
};
//post请求
const post = (api, data,header) => {
    return request('POST', api, data,header)
};
//缓存get请求
const cacheGet = (api, data) => {
    return cacheRequest('GET', api, data)
};
//缓存post调用请求
const cachePost = (api, data) => {
    return cacheRequest('POST', api, data)
};

const cache = (method, api, data, header,responseHook) => {
    return func.cacheRequest({ method, api, data, header,responseHook },(response)=>{
        return new Promise((resolve)=>{
            console.log('local',response)
            resolve(response)
        })
    },_request,60 * 60 * 1000)
}
export default {requestWithHook, get, cacheGet, post, cachePost,requestCommon,cache}