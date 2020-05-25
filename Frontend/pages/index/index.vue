<template>
	<view class="content">
		<home :scrollY="scrollY" :scrollBottom="scrollBottom && tabID == 0" :show="tabID == 0" v-if="loaded[0]"></home>
		<notice :scrollBottom="scrollBottom && tabID == 1" :show="tabID == 1" v-if="loaded[1]"></notice>
		<release :show="tabID == 2"  v-if="loaded[2]" @closeReleaseTap="closeReleaseTap" @switchTab="switchTab"></release>
		<news :scrollBottom="scrollBottom && tabID == 3" :show="tabID == 3" v-if="loaded[3]"></news>
		<my  :show="tabID==4" v-if="loaded[4]"></my>
		<!--底部导航-->
		<footer-tabbar :tabID="tabID" :msgDot="true" @tabTap="tabTap" v-show="isTabShow"/>
	</view>
</template>

<script>
import home from '../../components/view/home.vue'
import notice from '../../components/view/notice.vue'
import footerTabbar from '../../components/footer/footer-tabbar.vue'
import release from '../../components/view/release.vue'
import news from '../../components/view/news.vue'
import my from '../../components/view/my.vue'
export default {
	components: {
		home,
		notice,
		release,
		news,
		my,
		footerTabbar
	},
	data() {
		return {
			scrollY: 0,
			isTabShow: true,
			scrollBottom: false,
			prevTabID: 0,
			tabID: 4,
			loaded: [true,false,false, false, true]
		}
	},
	onLoad() {},
	onPageScroll(e) {
		this.scrollY = e.scrollTop
		this.scrollBottom = false
	},
	onReachBottom() {
		this.scrollBottom = true
	},
	methods: {
		switchTab(show){
			this.isTabShow = show
		},
		tabTap(index) {
			this.prevTabID = this.tabID
			this.tabID = index
			if (!this.loaded[index]) this.loaded[index] = true
		},
		closeReleaseTap(){
			this.tabID = this.prevTabID
		}
	}
}
</script>

<style></style>
