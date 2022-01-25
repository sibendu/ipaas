import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
function CustomerView(props) {
    const [customer, setCustomer] = useState(null);
    useEffect(() => {  
        console.log("passed customerid:"+ props.Customerid); 
        const requestbody = {
            service:'COMS-CUSTOMER-API',    
            operation:'/getcustomerbyid/'+ props.Customerid,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody)
                  .then(response => setCustomer(response.data));     
    },[props.Customerid]);
    if (!customer) return null;
    return (
        <div>
                <dl class="row">
                    <dt class="col-sm-2">Customer Id</dt>
                    <dd class="col-sm-10">{customer.customer_id}</dd>
                    <dt class="col-sm-2">Account Number</dt>
                    <dd class="col-sm-10">{customer.account_number}</dd>
                    <dt class="col-sm-2">First name</dt>
                    <dd class="col-sm-10">{customer.account_number}</dd>
                    <dt class="col-sm-2">Last name</dt>
                    <dd class="col-sm-10">{customer.first_name}</dd>
                    <dt class="col-sm-2">Company</dt>
                    <dd class="col-sm-10"> {customer.last_name}</dd>
                    </dl>
           </div>
    );
}
export default CustomerView;