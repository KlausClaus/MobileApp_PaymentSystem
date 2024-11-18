import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        // Stores the current route name for use across the application
        currentPathName: ''
    },
    mutations: {
        // Updates the `currentPathName` state with the value stored in localStorage
        setPath(state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        }
    }
})

export default store
