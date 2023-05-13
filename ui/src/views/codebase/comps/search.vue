<!--  -->
<template>
    <div class="flex">
        <el-input
            v-model="title"
            placeholder="title"
            @input="handleKeydown"
        ></el-input>
        <el-button @click="search">search</el-button>
    </div>
</template>

<script>
export default {
    components: {},
    data() {
        return {
            title: '',
            lang: '',
            delay: 100, // Delay in milliseconds
            timeoutId: null,
        }
    },
    computed: {},
    watch: {
        /**
         * handle changes when re-entering the page, mainly to obtain new paths
         */
        $route: {
            handler(new_route) {
                this.populateQueryParam(new_route)
            },
        },
    },
    created() {
        this.populateQueryParam(this.$route)
    },
    methods: {
        handleKeydown() {
            debugger
            // Clear the previous timeout (if any)
            clearTimeout(this.timeoutId)

            // Set a new timeout
            this.timeoutId = setTimeout(() => {
                // Perform some action here
                console.log(`User typed: ${this.title}`)
                this.search()
            }, this.delay)
        },
        populateQueryParam(route) {
            var query = route.query
            var title = query.title
            if (title != undefined) {
                this.title = title
            }
        },
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
