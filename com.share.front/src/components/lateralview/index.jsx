import Taro, { Component } from '@tarojs/taro'
import { View, Text, ScrollView } from '@tarojs/components'
import styles from './index.module.scss'

export default class SearchView extends Component {

  constructor(props) {
    super(props)
    this.state = {
      tabList:[{id:1,name:'食品',select:true},{id:2,name:'美妆',select:false},{id:1,name:'男装',select:false},{id:3,name:'女装',select:false},{id:4,name:'母婴',select:false},{id:5,name:'家居家装',select:false},{id:6,name:'数码家电',select:false},{id:7,name:'户外运动',select:false}]
    }
  }

  itemOnClickListener(item){
    let newTabList=this.state.tabList
      newTabList.forEach(item1 => {
      if(item1.name===item.name){
        item1.select=true
      }else{
        item1.select=false
      }
     });
     this.setState({tabList:newTabList},()=>{
      this.props.currentTabItem(item)
     })
    
  }

  getItem(){
    return this.state.tabList.map((item,index)=>{
      return <Text className={item.select?styles['tabTextSelect']:styles['tabTextUnSelect']} onClick={this.itemOnClickListener.bind(this,item)}>{item.name}</Text>
    })

  }
  
  render () {
    return (
      <View className={styles['search-page']}>
        <ScrollView scrollX={true} className={styles['scrollView']} >
         {this.getItem()} 
        </ScrollView>
      </View>
    )
  }
}
