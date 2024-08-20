import { useState } from "react"

export function SignUp() {

    const [username, setUsername] = useState()
    const [email, setEmail] = useState()
    const [password, setPassword] = useState()
    const [passwordRepeat, serPasswordRepeat] = useState()

    return (
        <> 
        <h1>Sign Up</h1>
        <div>
            <label htmlFor="username">Username</label>
            <input id="username"onChange={(event)=> setUsername(event.target.value)}/>
        </div>
        <div>
            <label htmlFor="email">Email</label>
            <input id="email"  onChange={(event)=> setEmail(event.target.value)}/>
        </div>
        <div>
            <label htmlFor="password">Password</label>
            <input id="password"onChange={(event) => setPassword(event.target.value)} />
        </div>
        <div>
            <label htmlFor="passwordRepeat">Repeat Password</label>
            <input id="passwordRepeat" onChange={(event)=> serPasswordRepeat(event.target.value)} />
        </div>
        <button disabled= {password && password!= passwordRepeat}>Sign Up</button>

      </>
     
    )
}