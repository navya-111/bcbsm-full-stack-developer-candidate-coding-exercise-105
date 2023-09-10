import { Injectable, inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterState, RouterStateSnapshot } from "@angular/router";
import { environment } from "./environment";
import { AuthService } from "./service/auth.service";


@Injectable({
    providedIn: "root"
})

class AuthGuard {

    constructor(private router: Router, private authservice: AuthService) { }
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        if (state.url == '/login"') return true;

        let token = sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME);

        if (!token) {
            this.router.parseUrl('/login');
            return false;
        } 

        return true;
    }
}

export const isAuthGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean => {
    return inject(AuthGuard).canActivate(route, state);
}