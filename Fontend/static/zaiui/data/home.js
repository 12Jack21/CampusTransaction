//首页虚拟数据库，仔仔编写。

let _home_data = {
	swiper() {
		return [
			{
				swiper: '/static/images/home/swiper/swiper-1.png',
				background: '/static/images/home/swiper/swiper-background-1.png',
			},
			{
				swiper: '/static/images/home/swiper/swiper-2.png',
				background: '/static/images/home/swiper/swiper-background-2.png',
			},
			{
				swiper: '/static/images/home/swiper/swiper-3.png',
				background: '/static/images/home/swiper/swiper-background-3.png',
			}
		];
	},
	nav() {
		return [{
			id: 1,
			name: '电子产品',
			color: 'orange',
			badge: '必看',
			img: '/static/images/home/grid-icon/1.png',
		},{
			id: 4,
			name: '数码',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/4.png',
		},{
			id: 5,
			name: '家电',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/5.png',
		},{
			id: 6,
			name: '衣物',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/5.png',
		},{
			id: 7,
			name: '鞋子',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/5.png',
		},{
			id: 7,
			name: '生活用品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/5.png',
		},{
			id: 7,
			name: '食品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/5.png',
		},{
			id: 8,
			name: '图书',
			color: 'red',
			badge: '必看',
			img: '/static/images/home/grid-icon/8.png',
		},{
			id: 9,
			name: '虚拟物品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/9.png',
		},{
			id: 10,
			name: '交通工具',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/10.png',
		},{
			id: 11,
			name: '作业',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/11.png',
		},{
			id: 12,
			name: '代领',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/12.png',
		},{
			id:13,
			name: '问答',
			color: 'red',
			badge: '解惑',
			img: '/static/images/home/grid-icon/12.png'
		},{
			id:14,
			name: '其他',
			color: 'green',
			badge: '更多',
			img: '/static/images/home/grid-icon/12.png'
		}];
	},
	sellQuickly() {
		return [
			{
				id: 1,
				title: '手机保卖',
				text: '99%卖出',
				img: '/static/images/home/sundry/6.png',
			},
			{
				id: 2,
				title: '拍卖报名',
				text: '24小时高价卖',
				img: '/static/images/home/sundry/7.png',
			},
			{
				id: 3,
				title: '扫码读书',
				text: '好书高价卖',
				img: '/static/images/home/sundry/8.png',
			}
		];
	},
	goodsTab() {
		return [
			{
				title: '最新',
				tag: '',
			},
			{
				title: '逛附近',
				tag: '',
			},
			{
				title: '即将失效',
				tag: '尽快收',
			},{
				title: '便宜好物',
				tag: '',
			}
		];
	},
	goodsList() {
		return [
			{
				v: false,
				mold: [],
				price: '2280.12',
				originalPrice:'12.3',
				state: '',
				username: '仔仔',
				time: '1小时前发布',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/1.png',
				avatar: '/static/images/avatar/1.jpg'
			},
			{
				v: true,
				mold: [{bg:'red',title: '剩余1件'}],
				price: '5049',
				originalPrice:'12.3',
				state: '发布中',
				username: '正品保障',
				time: '1分钟前发布',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/2.png',
				avatar: '/static/images/avatar/2.jpg'
			},
			{
				v: false,
				mold: [{bg:'blue',title: '剩余2件'}],
				price: '2980',
				originalPrice:'12.3',
				state: '已预约',
				username: '仔仔',
				time: '40分钟前发布',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/3.png',
				avatar: '/static/images/avatar/3.jpg'
			},
			{
				v: false,
				mold: [],
				price: '2280',
				originalPrice:'',
				state: '',
				username: '仔仔',
				time: '1个小时前来过',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/4.png',
				avatar: '/static/images/avatar/4.jpg'
			},
			{
				v: true,
				mold: [{bg:'red',title: '自营'}],
				price: '5049',
				originalPrice:'12.3',
				state: '支持验机',
				username: '正品保障',
				time: '7天无理由',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/5.png',
				avatar: '/static/images/avatar/5.jpg'
			},
			{
				v: false,
				mold: [],
				price: '2980',
				originalPrice:'12.3',
				state: '已验机',
				username: '仔仔',
				time: '当前在线',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/6.png',
				avatar: '/static/images/avatar/6.jpg'
			},
			{
				v: false,
				mold: [],
				price: '2280',
				originalPrice:'12.3',
				state: '',
				username: '仔仔',
				time: '1个小时前来过',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '/static/images/home/goods/7.png',
				avatar: '/static/images/avatar/7.jpg'
			}
		];
	},
	recommend() {
		return {
			type: 'recommend',
			list: [
				{
					title: '商品标题',
					img: '/static/images/home/goods/16.png',
				},
				{
					title: '商品标题',
					img: '/static/images/home/goods/15.png',
				},
				{
					title: '商品标题',
					img: '/static/images/home/goods/14.png',
				},{
					title: '商品标题',
					img: '/static/images/home/goods/13.png',
				}
			]
		};
	},
	gridSortData() {
		return [{
			id: 1,
			name: '手机',
			img: '/static/images/home/grid-icon/16.png',
		},{
			id: 2,
			name: '平板',
			img: '/static/images/home/grid-icon/17.png',
		},{
			id: 3,
			name: '电脑',
			img: '/static/images/home/grid-icon/18.png',
		},{
			id: 4,
			name: '数码',
			img: '/static/images/home/grid-icon/19.png',
		},{
			id: 5,
			name: '家电',
			img: '/static/images/home/grid-icon/20.png',
		},{
			id: 6,
			name: '新人红包',
			img: '/static/images/home/grid-icon/21.png',
		},{
			id: 7,
			name: '手机直播',
			img: '/static/images/home/grid-icon/22.png',
		},{
			id: 8,
			name: '自营图书',
			img: '/static/images/home/grid-icon/23.png',
		},{
			id: 9,
			name: '游戏',
			img: '/static/images/home/grid-icon/24.png',
		}];
	}
};

export default _home_data;