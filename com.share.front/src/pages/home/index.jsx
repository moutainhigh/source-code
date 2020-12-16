import Taro, { Component } from '@tarojs/taro'
import { View, Text, Input,Image } from '@tarojs/components'
import styles from './index.module.scss'
import SearchView from '../../components/lateralview/index'
import searchImg from '../../assets/search.png'
import TQNetwork from '../../kernel/TQNetwork'
import {FIND_LIST } from '../../utils/action'


export default class Home extends Component {

  constructor(props) {
    super(props)
    this.state = {
        res:''
    }
  }

  getItem(){
    <View className={'item'}> 
    </View>
  }

  componentDidMount(){
    this.getFindList()
  }

  getCurrentItem(item){
   console.log(item.name)
  }


  getFindList(){
    let params = {
        data: {}
    }
    TQNetwork.defaultApi().post(FIND_LIST, params).then(res => {
    
    
    }).catch(err => {
        console.log(err)
    }) 
}

  render () {
    return (
      <View className={styles['home-page']}>

        <View className={styles['search-view']}>
          <Text className={styles['search-view-current-platform']}>淘宝</Text>
          <Input className={styles['search-view-search']} placeholder={'请输入商品名称'}></Input>
          <Image className={styles['search-view-img']} src={searchImg}></Image>
        </View>
        <SearchView currentTabItem={this.getCurrentItem.bind(this)}/>
        {this.getItem()}
      </View>
    )
  }
}
