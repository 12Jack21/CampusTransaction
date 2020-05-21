//首页虚拟数据库，仔仔编写。

let _home_data = {
	swiper() {
		return [
			{
				swiper: '/static/images/home/swiper/1.png',
				background: '/static/images/home/swiper/swiper-background-1.png',
			},
			{
				swiper: '/static/images/home/swiper/2.png',
				background: '/static/images/home/swiper/swiper-background-2.png',
			},
			{
				swiper: '/static/images/home/swiper/3.png',
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
			id: 2,
			name: '数码',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/4.png',
		},{
			id: 3,
			name: '家电',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/jiadian.png',
		},{
			id: 4,
			name: '衣服',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/clothes.png',
		},{
			id: 5,
			name: '鞋子',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/shoe.png',
		},{
			id: 6,
			name: '生活用品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/yongpin.png',
		},{
			id: 7,
			name: '食品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/food.png',
		},{
			id: 8,
			name: '图书',
			color: 'red',
			badge: '必看',
			img: '/static/images/home/grid-icon/8.png',
		},{
			id: 9,
			name: '交通工具',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/10.png',
		},{
			id: 10,
			name: '护肤品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/hufu.png',
		},{
			id: 11,
			name: '虚拟物品',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/9.png',
		},{
			id: 12,
			name: '作业',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/11.png',
		},{
			id: 13,
			name: '代领',
			color: '',
			badge: '',
			img: '/static/images/home/grid-icon/27.png',
		},{
			id:14,
			name: '问答',
			color: 'red',
			badge: '解惑',
			img: '/static/images/home/grid-icon/28.png'
		},{
			id:15,
			name: '其他',
			color: 'green',
			badge: '更多',
			img: '/static/images/home/grid-icon/25.png'
		}];
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
				id:1,
				v: false,
				count: 1,
				price: '2280.12',
				originalPrice:'12.3',
				state: '',
				time: '1小时前发布',
				title: '商品标题，商品标题',
				img: '/static/images/home/goods/1.png',
				username: '仔仔',
				avatar: '/static/images/avatar/1.jpg'
			},
			{
				id:2,
				v: true,
				count: 2,
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
				id:3,
				v: false,
				count: 3,
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
				id:4,
				v: false,
				count: 4,
				price: '2280',
				originalPrice:'',
				state: '',
				username: '仔仔',
				time: '1个小时前来过',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '',
				avatar: '/static/images/avatar/4.jpg'
			},
			{
				id:5,
				v: true,
				count: 5,
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
				id:6,
				v: false,
				count: 6,
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
				count: 7,
				price: '2280',
				originalPrice:'12.3',
				state: '',
				username: '仔仔',
				time: '1个小时前来过',
				title: '商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题，商品标题',
				img: '',
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
};

export default _home_data;