import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import axios from 'axios';
import { environment } from '../environment';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  user: string;
  fromEmail: string;
  lines: any = [];
  linesR: any = [];
  file: File = null;
  hideCompose: boolean = true;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  emailItems
    : Array<any>;

  emailform = new FormGroup({
    toemail: new FormControl('', [Validators.email]),
    subject: new FormControl(''),
    message: new FormControl('')
  })
  constructor(private authService: AuthService) {

  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  ngOnInit(): void {
    this.user = this.authService.getLoggedInUserName();
    this.fromEmail = this.authService.getLoggedInUserId();
   
    this.getEmailList();
  }

  getEmailList() {
    let headers = {
      "Content-Type": 'application/json',
      "Access-Control-Allow-Origin": '*',
      "Authorization": sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME),
      "userid": sessionStorage.getItem(environment.LOGGEDUSER)
    };

    axios.get(environment.backendurl + "/email/getmails", { "headers": headers }).then(response => {

      this.dtOptions.destroy = true;
      this.emailItems
        = response.data;
      this.dtTrigger.next(response.data);
      return true;
    })
      .catch(error => {
        console.error(error);
        return false;
      });
  }

  onFileChanged(event) {
    this.file = event.target.files[0];
  }

  sendEmail() {
    let formParams = new FormData();
    formParams.append('file', this.file);

    const emailData = {
      "fromEmail": this.fromEmail,
      "toEmail": this.emailform.value['toemail'],
      "subject": this.emailform.value['subject'],
      "message": this.emailform.value['message']
    }

    formParams.append('emaildata', JSON.stringify(emailData));

    let headers = {
      "Content-Type": 'multipart/form-data',
      "Access-Control-Allow-Origin": '*',
      "Authorization": sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME),
      "userId": sessionStorage.getItem(environment.LOGGEDUSER)
    };

    axios.post(environment.backendurl + "/email/send", formParams, { "headers": headers }).then(response => {
      alert(response.data);
      this.hideCompose = true;
      this.getEmailList();
    })
      .catch(error => {
        console.error(error);
        return false;
      });
  }

  newMail() {
    this.hideCompose = false;
  }

  download(filedata) {
    let headers = {
      "Content-Type": 'application/json',
      "Access-Control-Allow-Origin": '*',
      "Authorization": sessionStorage.getItem(environment.SESSION_ATTRIBUTE_NAME),
      "userId": sessionStorage.getItem(environment.LOGGEDUSER)
    };

    axios.post(environment.backendurl + "/email/download", filedata, { "headers": headers }).then(response => {
      const temp = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = temp;
      link.setAttribute('download', filedata.filename); //or any other extension
      document.body.appendChild(link);
      link.click();
    })
      .catch(error => {
        console.error(error);
        return false;
      });
  }

  logout() {
    this.authService.logout();
  }

  validFileData(filedata) {
    return filedata==null;
  }
}
