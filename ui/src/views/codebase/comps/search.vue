<!--  -->
<template>
    <div class="flex">
        <el-input v-model="title" placeholder="title"></el-input>
        <el-select v-model="lang">
            <el-option value="" label="All"></el-option>
            <el-option value="javascript" label="javascript">
                javascript
            </el-option>
            <el-option value="java" label="java">java</el-option>
            <el-option value="kotlin" label="kotlin">kotlin</el-option>
        </el-select>
        <el-button @click="search">search</el-button>
    </div>
</template>

<script>
export default {
    components: {},
    data() {
        return { title: '', lang: '' }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    beforeCreate() {},
    beforeMount() {},
    beforeUpdate() {},
    updated() {},
    beforeDestroy() {},
    destroyed() {},
    activated() {},
    methods: {
        search() {
            if (this.title === '') {
                this.title = undefined
            }

            if (this.lang === '') {
                this.lang = undefined
            }

            this.$emit('search-hit', { title: this.title, lang: this.lang })
        },
    },
}
</script>
<style lang="scss" scoped>
@import '~@/styles/common-style.scss';
</style>
