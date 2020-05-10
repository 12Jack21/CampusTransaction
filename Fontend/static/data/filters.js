// 数据格式,数据中只需要包含以下字段和数据格式,开发者可以添加字段,比如id等等,不影响组件显示,
// 组件的返回结果是有菜单数组下标形式返回,
// 如果传入数据中有value,也会返回value,开发者可根据返回的下标获取所选中的菜单
/*
[
	{
		"name":"",	//字符串类型 选填项 菜单名称,如不填,则取第一个子菜单的name值,filter和radio类型则将设置为"筛选"
		"type":""	//字符串类型 必填项 可取值 hierarchy/filter/radio  hierarchy单/多层级菜单(最多三级); filter筛选多选菜单; radio筛选单选菜单
		"submenu":[	//对象数组类型 必填项 子菜单数据
			{
				"name":"",	//字符串类型 必填项 菜单名称
				"value":"",	//字符串类型 选填项 自定义内容,比如id等等,如果填写了,confirm返回的结果中将返回对应选中的value,若菜单无value字段则返回null,filter类型此字段无效果
				"submenu":[	//对象数组类型 必填项 子菜单数据
					{
						"name":"",	//字符串类型 必填项 菜单名称
						"value":"",	//字符串类型 选填项 自定义内容,比如id等等,如果填写了,confirm返回的结果中将返回对应选中的value,若菜单无value字段则返回null
						"submenu":[	//对象数组类型 必填项 子菜单数据 filter类型无效 
							{
								"name":"",	//字符串类型 必填项 菜单名称 hierarchy类型层级最多到此
								"value":"",	//字符串类型 选填项 自定义内容,比如id等等,如果填写了,confirm返回的结果中将返回对应选中的value,若菜单无value字段则返回null
							}
						]
					}
				]
			}
		]
	}
]
*/

//0.0.4版本起 返回结果将有两部分组成:
/*
{
	index:[],	//旧版本的下标数组形式
	value:[]	//菜单中的valve,结构和下标结果数组一样,只是把下标替换成了value而已
}
*/
// 以下演示数据中,我故意把value设置成跟name一样,只是为了方便演示,使示例更加易懂,实际使用时候value应该是一个标识,给后台识别所用的.
// 数据较长，请仔细查看。
export default [{
		// name:'分类',
		"type": 'hierarchy',
		"submenu": [{
			"name": '分类',
			"value": "分类",
			"submenu": [{
					"name": "全部",
					"value": "全部"
				},
				{
					"name": "电子产品类",
					"value": "电子产品类"
				},
				{
					"name": "数码类",
					"value": "数码类"
				},
				{
					"name": "家电类",
					"value": "家电类"
				},
				{
					"name": "衣服类",
					"value": "衣服类"
				},
				{
					"name": "鞋子类",
					"value": "鞋子类"
				},
				{
					"name": "生活用品类",
					"value": "生活用品类"
				},
				{
					"name": "食品类",
					"value": "食品类"
				},
				{
					"name": "图书",
					"value": "图书"
				},
				{
					"name": "交通工具",
					"value": "交通工具"
				},
				{
					"name": "虚拟物品",
					"value": "虚拟物品"
				},
				{
					"name": "作业",
					"value": "作业"
				},
				{
					"name": "代领",
					"value": "代领"
				},
				{
					"name": "问答",
					"value": "问答"
				},
				{
					"name": "其他",
					"value": "其他"
				},
			]
		}]
	},
	{
		// name:'区域',
		"type": 'hierarchy',
		"submenu": [{
				"name": "全校",
				"value": "全校"
			},
			{
				"name": "信息学部",
				"value": "信息学部"
			},
			{
				"name": "文理学部",
				"value": "文理学部"
			},
			{
				"name": "工学部",
				"value": "工学部"
			},
			{
				"name": "医学部",
				"value": "医学部"
			}
		]
	}, 
	{
		// name:'排序',
		"type": 'hierarchy',
		"submenu": [{
				"name": "最新",
				"value": "time"
			},
			{
				"name": "价格从低到高",
				"value": "price"
			},
			{
				"name": "失效时间从少到多",
				"value": "outdatedTime"
			},
			{
				"name": "发布者信誉值从高到低",
				"value": "evaluation"
			}
		]
	},
	{
		// name:'价格',
		"type": 'filter',
		"submenu": [{
				"name": "失效时间",
				"submenu": [{
						"name": "1天内",
						"value": "1天内"
					},
					{
						"name": "3天内",
						"value": "3天内"
					},
					{
						"name": "1周内",
						"value": "1周内"
					},
					{
						"name": "1月内",
						"value": "1月内"
					}
				]
			},
			{
				"name": "价格",
				"submenu": [{
						"name": "50以下",
						"value": "50以下"
					},
					{
						"name": "50-100",
						"value": "50-100"
					},
					{
						"name": "100-300",
						"value": "100-300"
					},
					{
						"name": "300以上",
						"value": "300以上"
					}
				]
			}
		]
	}
]
