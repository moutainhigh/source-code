
//为 undefined 时，也会得到与 null 相同的结果，虽然 null 和 undefined 不一样
const checkNotNull=(object)=>{
    if(object===null){
        throw new Error('check it is undefined');
    }
};

//检查字符串是否为 空（无字符,仅空格，null，undefined）,为空时返回true，参考android的TextUtil.isEmpty
const isEmpty=(str)=>{
    return str === null || '/^\s*$/'.match(str);
};


export default {checkNotNull,isEmpty}