#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <stdbool.h>
#define MAXCHAR 512
char* integer_to_str(int i){
int length= snprintf(NULL,0,"%d",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%d",i);
return result;
}
char* real_to_str(float i){
int length= snprintf(NULL,0,"%f",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%f",i);
return result;
}
char* char_to_str(char i){
int length= snprintf(NULL,0,"%c",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%c",i);
return result;
}
char* bool_to_str(bool i){
int length= snprintf(NULL,0,"%d",i);
char* result=malloc(length+1);
snprintf(result,length+1,"%d",i);
return result;
}
char* str_concat(char* str1, char* str2){
char* result=malloc(sizeof(char)*MAXCHAR);
result=strcat(result,str1);
result=strcat(result,str2);
return result;}

char* read_str(){
char* str=malloc(sizeof(char)*MAXCHAR);
scanf("%s",str);
return str;}

int str_to_bool(char* expr){
int i=0;
if ( (strcmp(expr, "true")==0) || (strcmp(expr, "1"))==0 )
i=1;
if ( (strcmp(expr, "false")==0) || (strcmp(expr, "0"))==0 )
i=0;
return i;}
int c=1;
char* stampa(char* messaggio) {
int i=0;
while (i<4) {
printf("\n");
i = i+1;
}
printf("%s", messaggio);
return "ok";
}
void sommac(int a, int d, float b, char* size, float* result) {
*result = a+b+c+d;
if(*result>100) {
char* valore=strdup("grande");
strcpy(size,valore);
}
  else if (*result>50) {
char* valore=strdup("media");
strcpy(size,valore);
}
 else {
char* valore=strdup("piccola");
strcpy(size,valore);
}
}
void main(int argc, char *argv[]) {
float risultato=0.0;
char* ans=strdup("no");
char* taglia = (char *)malloc(100 * sizeof(char));
char* ans1 = (char *)malloc(100 * sizeof(char));
int x=3;
int a=1;
float b=2.2;
char* valore=strdup("nok");
sommac(a,x,b,taglia,&risultato);
free(valore);
valore = strdup(stampa(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("la somma di ", integer_to_str(a))," e "), real_to_str(b))," incrementata di "), integer_to_str(c))," e' "),taglia)))
;
free(valore);
valore = strdup(stampa(str_concat("ed e' pari a ", real_to_str(risultato))))
;
printf("vuoi continuare? (si/no) - inserisci due volte la risposta\n");
scanf("%s",ans);
scanf("%s",ans1);
while (strcmp(ans,"si")==0) {
printf("inserisci un intero:");
scanf("%d",&a);
printf("inserisci un reale:");
scanf("%f",&b);
sommac(a,x,b,taglia,&risultato);
free(valore);
valore = strdup(stampa(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("la somma di ", integer_to_str(a))," e "), real_to_str(b))," incrementata di "), integer_to_str(c))," e' "),taglia)))
;
free(valore);
valore = strdup(stampa(str_concat(" ed e' pari a ", real_to_str(risultato))))
;
printf("vuoi continuare? (si/no):\t");
scanf("%s",ans);
}
printf("\n");
printf("ciao ");
}
