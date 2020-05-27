let _release_data = {
	noticeTypeListData() {
		return [{
			id: 1,
			title: '发布出售通告',
			text: '含闲置物品列表',
			img: '/static/images/home/grid-icon/1.png'
		},{
			id: 2,
			title: '发布需求通告',
			text: '含需求列表',
			img: '/static/images/home/grid-icon/27.png'
		},{
			id: 3,
			title: '发布任务通告',
			text: '取快递/回答作业题',
			img: '/static/images/home/grid-icon/28.png'
		}];
	},
	comTypeData(){
		return ['电子产品','数码', '家电', '衣服','鞋子','生活用品','食品','图书','交通工具类', 
			'护肤品','虚拟物品','作业','代领','问答','其他']
	},
	newnessData(){
		return ['全新', '九五新', '九成新', '八五新', '八成新', '七成新']
	}
};

export default _release_data;