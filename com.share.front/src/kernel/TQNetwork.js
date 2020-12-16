import HttpClient from './http/HttpClient';
import ApiResponse from './http/ApiResponse';
let selfDefaultApi;

function makeUrl(baseUrl,api){

    console.log(process.env.NODE_ENV)
    if(process.env.NODE_ENV==='development'){
        return `${'/'}${api}`
    }else{
        return `${baseUrl+'/'}${api}`
    } 
}

class Api{

    constructor(_baseUrl) {
        this.baseUrl = _baseUrl
        this.code_ok = 200
    }

    setCodeOk(_code_ok){
        this.code_ok = _code_ok
    }   

    hookRequest(method,header,data) {
        return this.hookRequestData(data)
    }
    //  加入自定义header
    hookHeader(action,header,data) {
        let _header = (data && data.header) || {}
        if ('none' === _header){ // 传none时, 一个header也不
            header = _header = {}
        }
        return Object.assign(header,_header)
    }

    hookRequestData(data) {
        return (data&&data.data)||{}
    }

    hookResponseData(resData){
        let response = null;
        if(resData!==null&&resData!==undefined){
            if(resData.code!=null){
                response = new ApiResponse()
                response.setResponse(resData.code,resData.data,resData.msg,resData);
                if(response.code===200){
                    response.setIsSuccess(true)
                }else{
                    response.setIsSuccess(false)
                }
                return response
            }
        }
        response = new ApiResponse()
        response.setIsSuccess(true)
        response.setResponse(200,null,null,resData)
        return response
    }

    get(api,data,tag=''){
        let action=makeUrl(this.baseUrl,api)
        let header={ 
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        header = this.hookHeader(action,header,data)
        data = this.hookRequest('GET',header,data);
        return HttpClient.requestWithHook('GET',action,data,header,this.hookResponseData);
    }
    
    post(api,data){
        let action=makeUrl(this.baseUrl,api)
            console.log(action)
        
        let header={
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        header = this.hookHeader(action,header,data)
        data = this.hookRequest('POST',header,data);

        return HttpClient.requestWithHook('POST',action,data,header,this.hookResponseData);
    }

    request(method,api,data){
        let action=makeUrl(this.baseUrl,api)
        // console.log(action)
        let header={
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        header = this.hookHeader(action,header,data)
        data = this.hookRequest(method,header,data);

        return HttpClient.requestWithHook(method,action,data,header);
    }

    cacheGet(api,data,tag=''){
        let action=makeUrl(this.baseUrl,api)
        let header={
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        header = this.hookHeader(action,header,data)
        data = this.hookRequest('GET',header,data);
        return HttpClient.cache('GET',action,data,header,this.hookResponseData);
    }
    
    cachePost(api,data){
        let action=makeUrl(this.baseUrl,api)
            console.log(action)
        
        let header={
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        header = this.hookHeader(action,header,data)
        data = this.hookRequest('POST',header,data);

        return HttpClient.cache('POST',action,data,header,this.hookResponseData);
    }
}

const create=(_baseUrl,_appKey,_token)=>{
    return new Api(_baseUrl,_appKey,_token)
};

const createDefaultApi=(_baseUrl)=>{
    selfDefaultApi = new Api(_baseUrl);
}

const defaultApi=()=>{
    return selfDefaultApi;
}

export default {create,createDefaultApi,defaultApi,makeUrl}