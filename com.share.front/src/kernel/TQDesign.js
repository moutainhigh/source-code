
const showDialog=(message)=>{
    alert(message)
};

const showToast=(message)=>{
    // alert(message)

    // AtToast.showDialog(message)
};

const showLoading=(message='正在载入数据')=>{
    Toast.loading(message,0,null,true);
};

const hideLoading=()=>{
    Toast.hide();
};

const setTitle = title => () => document.title = title;

export default {showDialog,showToast,showLoading,hideLoading,setTitle}