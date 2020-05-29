// action: 1-取消了，2-评论了，3-确认了，5-成功结束了，6-即将失效，7-失效了，8-回复了，9-预约了, 10-失败了
// targetType: 0-商品，1-预约，2-评论，3-通告
export default [
	{
	    id: 1,
	    isRead: true,
	    createTime: '2020',
	    sender: 10,
	    avatar: '',
	    accountName: 'sam',
			accountGender: 1,
	    action: 1,
	    targetId: 100, // 预约
	    targetType: 1, 
	    commodity:{
	    	id: -1,
	    	name: '',
	    	img: ''
			},
			title: ''
	},
	{
	    id: 2,
	    isRead: false,
	    createTime: '2020-10-02',
	    sender: 11,
	    avatar: '',
	    accountName: 'sam',
	    action: 2, //评论了
	    targetId: 1001, // 物品
	    targetType: 0, 
	    commodity:{
	    	id: 21,
	    	name: '毛巾',
	    	img: ''
			},
			title: ''
	},
	{
	    id: 3,
	    isRead: false,
	    createTime: '2020-05-20',
	    sender: -1, 
	    avatar: '',
	    accountName: '',
			accountGender: 1,
	    action: 5,
	    targetId: 20, // 预约
	    targetType: 1, 
	    commodity:{
	    	id: -1,
	    	name: '',
	    	img: ''
			},
			title: ''
	},
	{
	    id: 4,
	    isRead: false,
	    createTime: '2020-05-20',
	    sender: 11, 
	    avatar: 'aa',
	    accountName: 'Tom',
			accountGender: 0,
	    action: 9,
	    targetId: 201, // 物品
	    targetType: 0, 
	    commodity:{
	    	id: 9,
	    	name: '牙刷',
	    	img: 'qwe'
			},
			title: ''
	},
	{
	    id: 5,
	    isRead: false,
	    createTime: '2020-05-20',
	    sender: -1, 
	    avatar: '',
	    accountName: '',
			accountGender: 0,
	    action: 7,
	    targetId: 20, // 通告
	    targetType: 3, 
	    commodity:{
	    	id: -1,
	    	name: '',
	    	img: ''
			},
			title: '毕业了'
	},
	{
	    id: 6,
	    isRead: true,
	    createTime: '2020-05-11',
	    sender: 12, 
	    avatar: '去',
	    accountName: 'Jackason',
	    action: 8,
	    targetId: 201, // 评论
	    targetType: 2, //回复
	    commodity:{
	    	id: 77,
	    	name: '珍珠',
	    	img: ''
			},
			title: ''
	},
]