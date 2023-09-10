import { Injectable } from "@angular/core";
import axios from "axios";
import { environment } from "../environment";
import { Router } from "@angular/router";


@Injectable({
    providedIn: 'root'
  })
  export class AuthService {
    register(value: Partial<{ name: string; email: string; password: string; }>) {
        let headers =  {
            "Content-Type": 'application/json',
            "Access-Control-Allow-Origin": '*',
        };

        axios.post(environment.backendurl + "/user/register", {"name": value.name, "email": value.email, "password": value.password},
        {"headers":headers}).then(response => {
                // Handle successful response
                console.log(response.data);
                alert("Registration Succesfull")
                return true;
            })
            .catch(error => {
                console.error(error);
                return false;
            });
    }

    constructor (private router:Router) {

    }
    login(value: Partial<{ email: string; password: string; }>) {
        let headers =  {
            "Content-Type": 'application/json',
            "Access-Control-Allow-Origin": '*',
        };

        axios.post(environment.backendurl + "/login", {"email": value.email, "password": value.password},
        {"headers":headers}).then(response => {
                // Handle successful response
                console.log(response.data);
                sessionStorage.setItem(environment.SESSION_ATTRIBUTE_NAME, response.data.token);
                sessionStorage.setItem(environment.LOGGEDUSER, value.email);
                sessionStorage.setItem(environment.USERNAME, response.data.userName);
                this.router.navigate(['/home']);
                return true;
            })
            .catch(error => {
                console.error(error);
                return false;
            });
    }

    logout() {
        let headers =  {
            "Content-Type": 'application/json',
            "Access-Control-Allow-Origin": '*',
            "Authorization" : sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME),
            "userId": sessionStorage.getItem(environment.LOGGEDUSER)
        };

        axios.post(environment.backendurl + "/logout", '',
        {"headers":headers}).then(response => {
                return true;
            })
            .catch(error => {
                console.error(error);
                return false;
            });


        sessionStorage.clear();
        this.router.navigate(['login']);
    }

    isUserLoggedIn() {
        let sessionId = sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME)
        if (sessionId === null) return false
        return true
    }

    getLoggedInUserId() {
        let user = sessionStorage.getItem(environment.LOGGEDUSER)
        if (user === null) return ''
        return user
    }

    getLoggedInUserName(): string {
        return sessionStorage.getItem(environment.USERNAME);
      }
  }