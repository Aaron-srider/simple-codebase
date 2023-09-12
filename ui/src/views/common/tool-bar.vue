<template>
    <div>
        <div class="flex align-center fs12 mgb8">
            <slot></slot>
        </div>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
@Component({})
export default class ToolBarView extends Vue {}
</script>

<style lang="scss">
@import '~@/style/common-style.scss';
</style>
