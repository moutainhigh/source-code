 //缓存调用结果
const cacheCall=(fn, interval)=> {
    let caches = [];
    setInterval(()=> {
        caches=caches.filter(cache=>!cache.pending && new Date() - cache.time > interval)
    }, 60 * 1000);
    return (arg, cb)=> {
        const done = (res)=> {
            const cache = caches.find(cache=> JSON.stringify(cache.arg)===JSON.stringify(arg));
            if (cache) {
                Object.assign(cache, { value: res, time: new Date(), pending: false });
                cache.list.forEach(cb=> cb(res));
                cache.list=[]
            }
        };
        const cache = caches.find(cache=> JSON.stringify(cache.arg)===JSON.stringify(arg));
        if (cache) {
            cache.pending ? cache.list.push(cb) : cb(cache.value)
        } else {
            caches.push({ arg: arg, pending: true, list: [cb] });
            fn(arg, done);
        }
    }
};

let caches = [];
const cacheRequest = (arg, local,remote,interval)=> {
    console.log('allCache',caches)
    caches=caches.filter(cache=>!cache.pending && new Date() - cache.time < interval)
    console.log('cache',caches)
    const done = (res)=> {
        const cache = caches.find(_cache=> JSON.stringify(_cache.arg)===JSON.stringify(arg));
        if (cache) {
            Object.assign(cache, { value: res, time: new Date(), pending: false });
            // cache.list.forEach(_cb=> _cb(res));
            cache.list=[]
        }
    };
    const cache = caches.find(_cache=> JSON.stringify(_cache.arg)===JSON.stringify(arg));
    if (cache) {
        if (cache.pending){
            cache.list.push(local)
        }else {
            return local(cache.value)
        } 
    } else {
        caches.push({ arg: arg, pending: true, list: [local] });
        return remote(arg, done);
    }
}
//尝试调用
const tryCall= (fn, interval, n)=> {
    return (done, fail)=> {
        const callFn = ()=> { fn(done, failFn); };
        const failFn = (err)=> { --n ? setTimeout(callFn, interval) : fail(err) };
        callFn();
    }
};

export default {cacheCall,tryCall,cacheRequest}