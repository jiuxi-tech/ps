/*!
* demoRouter
* (c) 2020 lincong1987
*/

let demoRouter = [

    {
        path: '/demo',
        children: [

            {
                path: '/button',
                component: {

                    template: `
                        <div>demo button</div>
                    `

                }
            },

        ],

    },

]

export default demoRouter
