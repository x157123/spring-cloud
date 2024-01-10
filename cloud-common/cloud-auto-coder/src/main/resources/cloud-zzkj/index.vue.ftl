<template>
    <div class="page-body">
        <a-card style="width: 100%;" :tab-list="tabList" :active-tab-key="tabKey" @tabChange="onTabChange">
            <!-- 待处理 -->
            <myMediationUntreated v-if="tabKey === '1'" :routeId="routeId"></myMediationUntreated>
        </a-card>
    </div>
</template>

<script>
    import myMediationUntreated from '@/components/mediatiionCenter/myMediationUntreated';
    export default {
        components: {
            myMediationUntreated,
        },
        data() {
            return {
                tabList: [
                    {
                        key: '1',
                        tab: '待处理',
                    },
                ],
                tabKey: '1',
                routeId: '', // 路由跳转传参ID
            };
        },
        methods: {
            // tab切换
            onTabChange(key) {
                this.tabKey = key;
            },
        },
        mounted() {
            if (this.$route.query.detailId) {
                this.routeId = this.$route.query.detailId;
            }
        },
    };
</script>
<style scoped>
    .page-body {
        padding: 30px 0;
    }
</style>
