const path = require('path')
const root = path.dirname(__dirname)
console.log('root path = ', root)
const utils = require('./utils/index')
const filehelper = require('./utils/initPage')

module.exports = {
  title: 'SE Blog',
  base: '/CampusTransaction/', // 设置站点根路径
  description: 'A blog for campus second transaction project',
  //theme:'reco',
  themeConfig: {
    nav: [
      {
        text: 'Github',
        link: 'https://github.com/12Jack21/CampusTransaction',
        target: '_blank',
      },
    ],
    sidebarDepth: 1,
    // displayAllHeaders: true,
    sidebar: {
      '/': [
        {
          title: '项目介绍',
          collapsable: false,
          children: ['', 'hom1.md', 'home2'],
        },
        {
          title: '需求分析',
          collapsable: false,
          children: ['Demand/'],
        },
        {
          title: 'G2',
          collapsable: false,
          children: ['page-b/', '/page-b/a.html'],
        },
      ],
    }
  },
  plugins:[
    ['@vuepress/search',{
      searchMaxSuggestion:6
    }],
    '@vuepress/medium-zoom',
    // '@limdongjin/vuepress-plugin-sidebar-on-off'
  ]
}
