const path = require('path')
const root = path.dirname(__dirname)
console.log('root path = ', root)
const utils = require('./utils/index')
const filehelper = require('./utils/initPage')

module.exports = {
  title: 'SE Blog',
  base: '/CampusTransaction/', // 设置站点根路径
  description: '面向在校大学生的综合性供需系统',
  head:[
	['meta',{name:'google-site-verification',content:'1EJBxWJB8NwAT0KYQkOvbzLAdK38PkPthYkHTQ9iSss'}]
  ],
  theme:'reco',
  themeConfig: {
    nav: [
      {
        text: 'Github',
        link: 'https://github.com/12Jack21/CampusTransaction',
        target: '_blank',
      },
    ],
    sidebarDepth: 2,
    // displayAllHeaders: true,
    sidebar: [
      ['/', '首页'],
      ['/Introduction', '项目介绍'],
      ['/Demand','需求说明'],
      ['/DataBase','数据库设计'],
      ['/System','系统设计'],
      ['/Test','测试结果']
    ]
    // sidebar: {
    //   '/': [
    //     {
    //       title: '项目介绍',
    //       collapsable: false,
    //       children: ['', 'hom1.md', 'home2'],
    //     },
    //     {
    //       title: '需求分析',
    //       collapsable: false,
    //       children: ['Demand/'],
    //     },
    //     {
    //       title: 'G2',
    //       collapsable: false,
    //       children: ['page-b/', '/page-b/a.html'],
    //     },
    //   ],
    // }
  },
  plugins:[
    ['@vuepress/search',{
      searchMaxSuggestion:6
    }],
    ['@vuepress/medium-zoom',{
      selector: 'img.zoom-custom-imgs',
      // medium-zoom options here
      // See: https://github.com/francoischalifour/medium-zoom#options
      options: {
        margin: 24
      }
    }],
	[
      '@vuepress/google-analytics',
      {
        'ga': 'UA-165698705-1' // UA-00000000-0
      }
    ]
    // '@limdongjin/vuepress-plugin-sidebar-on-off'
  ]
}
