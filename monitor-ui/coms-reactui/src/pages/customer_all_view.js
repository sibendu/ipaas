import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
function CustomerAllView(props) {
    const [customer, setCustomer] = useState([]);  
    
    useEffect(() => { 
        const requestbody = {
            service:'COMS-CUSTOMER-API',    
            operation:'/getallcustomers',
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody)
                  .then(response => setCustomer(response.data));             

    }, []); 
    const handleClick = (custid)=> {
        console.log('cust id clicked:'+ custid);
        props.handleCustomerViewClick("SingleCustomerView",custid);
      } 
    return (
       
            <table class="table">
                <thead class="thead-dark">
                    <tr>
                        <th scope="col">Id</th>
                        <th scope="col">Account #</th>
                        <th scope="col">First Name</th>
                        <th scope="col">Last Name</th>
                        <th scope="col">Company</th>
                    </tr>
                </thead>
                <tbody>
                        {customer.map(item => {  
                            return (
                                <tr>
                                    <th scope="row"><a href="#" onClick={()=>handleClick(item.customer_id)}>{item.customer_id}</a></th>
                                    <td>{item.account_number}</td>
                                    <td>{item.first_name}</td>
                                    <td>{item.last_name}</td>
                                    <td>{item.company_name}</td>
                                </tr>
                            )
                        })}  
                </tbody>
            </table>
      );
}
export default CustomerAllView;